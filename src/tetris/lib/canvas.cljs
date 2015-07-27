(ns ^:figwheel-always tetris.lib.canvas)

(def context
  (memoize
   (fn [canvas]
     (.getContext canvas "2d"))))

(defn clear!
  ([ctx]
   (let [canvas (.-canvas ctx)]
     (clear! ctx 0 0 (.-clientWidth canvas) (.-clientHeight canvas))))
  ([ctx x y w h]
   (.clearRect ctx x y w h)
   ctx))
