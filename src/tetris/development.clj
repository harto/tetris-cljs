(ns tetris.development
  (:require figwheel-sidecar.repl-api))

(defn repl []
  (figwheel-sidecar.repl-api/cljs-repl))
