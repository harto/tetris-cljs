(ns ^:figwheel-always tetris.main
    (:refer-clojure :exclude (drop update))
    (:require (tetris.command :as cmd)
              (tetris.lib.canvas :as canvas)
              (tetris.tetromino :as tetromino)
              (tetris.tile :as tile))
    (:use (cljs.core.async :only (<!))
          (tetris.core :only (canvas cols)))
    (:use-macros (cljs.core.async.macros :only (go))))

(enable-console-print!)

(defonce state
  (atom {:current (tetromino/random)}))

(defn rotate [state]
  (println "rotate")
  state)

(defn drop [state]
  (assoc-in state [:current] (tetromino/random)))

(defn move-left [state]
  (update-in state [:current :col] #(max 0 (dec %))))

(defn move-right [state]
  (let [max-col (- cols (tetromino/width (get state :current)))]
    (update-in state [:current :col] #(min max-col (inc %)))))

(def handlers {:rotate rotate
               :drop drop
               :move-left move-left
               :move-right move-right})

(defn apply-commands [state commands]
  (->> commands
       (map #(get handlers %))
       (reduce (fn [state f]
                 (f state))
               state)))

(defn update [state time]
  state)

(defn repaint [g state]
  (canvas/clear! g)
  (canvas/render (get state :current) g))

(defonce command-queue
  (atom #queue[]))

(defn tick [time]
  (let [commands @command-queue
        new-state (-> @state
                      (apply-commands commands)
                      (update time))]
    (reset! command-queue #queue[])
    (reset! state new-state)
    (repaint (canvas/context (canvas)) new-state)))

(defn install-command-listener []
  (let [commands (cmd/capture-commands!)]
    (go (while true
          (swap! command-queue conj (<! commands))))))

(defn repeatedly-request-animation-frame []
  (.requestAnimationFrame js/window
                          (fn [time]
                            (tick time)
                            (repeatedly-request-animation-frame))))

(defonce setup
  (do
    (install-command-listener)
    (repeatedly-request-animation-frame)))
