(ns ^:figwheel-always tetris.lib.keyboard
    (:require (clojure.string :as str)))

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
