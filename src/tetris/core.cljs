(ns ^:figwheel-always tetris.core)

(def canvas
  (memoize #(.getElementById js/document "canvas")))

(def cols 10)
(def rows 20)

(defn col-width []
  (/ (.-clientWidth (canvas)) cols))

(defn row-height []
  (/ (.-clientHeight (canvas)) rows))

(defn col->x [col]
  (* col (col-width)))

(defn row->y [row]
  (* row (row-height)))
