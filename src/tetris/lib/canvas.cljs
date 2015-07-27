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

(defn set-properties! [ctx props]
  (doseq [[k v] props]
    (aset ctx k v))
  ctx)

(defn fill-text! [ctx x y s]
  (.fillText ctx s x y)
  ctx)
