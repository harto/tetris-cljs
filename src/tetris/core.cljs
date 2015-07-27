(ns ^:figwheel-always tetris.core
    (:refer-clojure :exclude (update))
    (:require (tetris.lib.keyboard :as keyboard))
    (:use (cljs.core.async :only (<!)))
    (:use-macros (cljs.core.async.macros :only (go))))

(enable-console-print!)

(defonce state
  (atom nil))

(defonce event-queue
  (atom #queue[]))

(defn rotate-current-piece [state]
  (println "rotate-current-piece")
  state)

(defn drop-current-piece [state]
  (println "drop-current-piece")
  state)

(defn move-current-piece-left [state]
  (println "move-current-piece-left")
  state)

(defn move-current-piece-right [state]
  (println "move-current-piece-right")
  state)

(def commands {:up rotate-current-piece
               :down drop-current-piece
               :left move-current-piece-left
               :right move-current-piece-right})

(defn player-commands [key-events]
  (->> key-events
       (filter #(= :key-down (:type %)))
       (map #(:key %))
       (set)
       (map #(get commands % identity))))

(defn process-commands [state commands]
  (reduce (fn [state command] (command state))
          state
          commands))

(defn update [state time]
  state)

(defn tick [time]
  (let [commands (player-commands @event-queue)
        new-state (-> @state
                      (process-commands commands)
                      (update time))]
    (reset! event-queue #queue[])
    (reset! state new-state)))

(defn install-key-event-handler []
  (let [key-events (keyboard/capture-events! js/document)]
    (go (while true
          (swap! event-queue conj (<! key-events))))))

(defn repeatedly-request-animation-frame []
  (.requestAnimationFrame js/window
                          (fn [time]
                            (tick time)
                            (repeatedly-request-animation-frame))))

(defonce setup
  (do
    (install-key-event-handler)
    (repeatedly-request-animation-frame)))
