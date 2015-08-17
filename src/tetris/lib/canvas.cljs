(ns ^:figwheel-always tetris.lib.canvas)

(def context
  (memoize
   (fn [canvas]
     (.getContext canvas "2d"))))

(defn save! [ctx]
  (doto ctx (.save)))

(defn restore! [ctx]
  (doto ctx (.restore)))

(defn translate! [ctx x y]
  (doto ctx (.translate x y)))

(defn clear!
  ([ctx]
   (let [canvas (.-canvas ctx)]
     (clear! ctx 0 0 (.-clientWidth canvas) (.-clientHeight canvas))))
  ([ctx x y w h]
   (doto ctx (.clearRect x y w h))))

(defn set-properties! [ctx props]
  (doseq [[k v] props]
    (aset ctx k v))
  ctx)

(defn fill-rect! [ctx x y w h]
  (doto ctx (.fillRect x y w h)))

(defprotocol Renderable
  (render [this ctx]))
