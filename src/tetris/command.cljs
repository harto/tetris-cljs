(ns ^:figwheel-always tetris.command
    (:require (domina.events :as evt)
              (tetris.lib.keyboard :as kbd))
    (:use (cljs.core.async :only (chan put!))))

(defn rotate-current-piece [state]
  (println "rotate-current-piece")
  state)

(defn drop-current-piece [state]
  (println "drop-current-piece")
  state)

(defn move-current-piece-left [state]
  (println "move-current-piece-left")
  (update-in state [:current :col] dec))

(defn move-current-piece-right [state]
  (println "move-current-piece-right")
  (update-in state [:current :col] inc))

(def commands {:up rotate-current-piece
               :down drop-current-piece
               :left move-current-piece-left
               :right move-current-piece-right})

(defn get-command [e]
  (let [pressed-key (kbd/code->name (:keyCode e))]
    (get commands pressed-key)))

(defn capture-commands! []
  (let [chan (chan)]
    (evt/listen! :keydown (fn [e]
                            (when-let [command (get-command e)]
                              (put! chan command)
                              (evt/prevent-default e))))
    chan))
