(ns ^:figwheel-always tetris.command
    (:require (domina.events :as evt)
              (tetris.lib.keyboard :as kbd))
    (:use (cljs.core.async :only (chan put!))))

(def commands {:up :rotate
               :left :move-left
               :right :move-right
               :space :drop})

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
