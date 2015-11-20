(ns ^:figwheel-always tetris.tile
    (:require (tetris.lib.canvas :as canvas))
    (:use (tetris.core :only (canvas col->x col-width row->y row-height))))

(defrecord Tile [col row color]
  canvas/Renderable
  (render [this g]
    (doto g
      (canvas/set-properties! {"fillStyle" color})
      (canvas/fill-rect! (col->x col) (row->y row) (col-width) (row-height)))))

(defn position [tile]
  (select-keys tile [:row :col]))
