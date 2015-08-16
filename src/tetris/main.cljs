(ns ^:figwheel-always tetris.main
    (:refer-clojure :exclude (update))
    (:require (tetris.command :as cmd)
              (tetris.lib.canvas :as canvas)
              (tetris.tetromino :as tetromino)
              (tetris.tile :as tile))
    (:use (cljs.core.async :only (<!))
          (tetris.core :only (canvas)))
    (:use-macros (cljs.core.async.macros :only (go))))

(enable-console-print!)

(defonce state
  (atom nil))

(defonce command-queue
  (atom #queue[]))

(defn apply-commands [state commands]
  (reduce (fn [state command] (command state))
          state
          commands))

(defn update [state time]
  state)

(defn repaint [g state]
  (canvas/clear! g)
  (canvas/render (tile/->Tile 50 50 "#f00") g))

(defn tick [time]
  (let [commands @command-queue
        new-state (-> @state
                      (apply-commands commands)
                      (update time))]
    (reset! command-queue #queue[])
    (reset! state new-state)
    (repaint (canvas/context (canvas)) new-state)))

(defn install-command-listener []
  (let [commands (cmd/capture-commands!)]
    (go (while true
          (swap! command-queue conj (<! commands))))))

(defn repeatedly-request-animation-frame []
  (.requestAnimationFrame js/window
                          (fn [time]
                            (tick time)
                            (repeatedly-request-animation-frame))))

(defonce setup
  (do
    (install-command-listener)
    (repeatedly-request-animation-frame)))
