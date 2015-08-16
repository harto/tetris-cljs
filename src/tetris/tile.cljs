(ns ^:figwheel-always tetris.tile
    (:require (tetris.lib.canvas :as canvas))
    (:use (tetris.core :only (canvas cols rows))))

(defn width []
  (/ (.-clientWidth (canvas)) cols))

(defn height []
  (/ (.-clientHeight (canvas)) rows))

(defrecord Tile [x y color]
  canvas/Renderable
  (render [this g]
    (doto g
      (canvas/set-properties! {"fillStyle" color})
      (canvas/fill-rect! x y (width) (height)))))
