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
  (atom {:tetromino (tetromino/random)}))

(defn valid? [state]
  (let [{:keys [tiles tetromino]} state]
    (and (tetromino/in-bounds? tetromino))))

(defn attempt [initial-state & modifiers]
  (let [new-state (first (filter valid? (for [f modifiers] (f initial-state))))]
    (if (valid? new-state)
      new-state
      initial-state)))

(defn rotate [state]
  (attempt state
           #(update-in % [:tetromino] tetromino/rotate)
           ;; wall kick
           #(update-in % [:tetromino] (comp tetromino/rotate tetromino/move-left))
           #(update-in % [:tetromino] (comp tetromino/rotate tetromino/move-right))))

(defn drop [state]
  (assoc-in state [:tetromino] (tetromino/random)))

(defn move-left [state]
  (attempt state #(update-in % [:tetromino] tetromino/move-left)))

(defn move-right [state]
  (attempt state #(update-in % [:tetromino] tetromino/move-right)))

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
  (canvas/render (get state :tetromino) g))

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
