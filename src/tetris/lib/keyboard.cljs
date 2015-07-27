(ns ^:figwheel-always tetris.lib.keyboard
    (:require (goog.events :as events)
              (goog.events.EventType :as EventType)
              (clojure.string :as str))
    (:use (cljs.core.async :only (chan put!))))

(def special-keys {13 :enter
                   32 :space
                   37 :left
                   38 :up
                   39 :right
                   40 :down
                   46 :delete})

(defn code->name [code]
  (or (special-keys code)
      (-> code
          (js/String.fromCharCode)
          (str/lower-case)
          (keyword))))

(defn create-event [type goog-event]
  {:type type
   :key (code->name (.-keyCode goog-event))})

(defn capture-events! [el]
  (let [chan (chan)]
    (events/listen el EventType/KEYDOWN #(put! chan (create-event :key-down %)))
    (events/listen el EventType/KEYUP #(put! chan (create-event :key-up %)))
    chan))
