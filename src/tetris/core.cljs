(ns ^:figwheel-always tetris.core
    (:refer-clojure :exclude (update))
    (:require (tetris.command :as cmd)
              (tetris.lib.canvas :as canvas))
    (:use (cljs.core.async :only (<!)))
    (:use-macros (cljs.core.async.macros :only (go))))

(enable-console-print!)

(def canvas
  (memoize #(.getElementById js/document "canvas")))

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
  (canvas/clear! g))

(defn tick [time]
  (let [commands @command-queue
        new-state (-> @state
                      (apply-commands commands)
                      (update time))]
    (reset! command-queue #queue[])
    (reset! state new-state)
    (repaint (canvas/context (canvas)) state)))

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
