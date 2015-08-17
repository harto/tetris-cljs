(ns tetris.lib.canvas-macros)

(defmacro with-restored-context [ctx & forms]
  ~(tetris.lib.canvas/with-restored-context* ctx #()))
