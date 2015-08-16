(ns ^:figwheel-always tetris.core)

(def canvas
  (memoize #(.getElementById js/document "canvas")))

(def cols 10)
(def rows 20)
