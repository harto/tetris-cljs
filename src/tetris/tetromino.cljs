(ns ^:figwheel-always tetris.tetromino
    (:require (tetris.lib.canvas :as canvas)
              (tetris.tile :as tile)))

(def layouts

  {:T ["###"
       " # "]

   :J [" #"
       " #"
       "##"]

   :L ["# "
       "# "
       "##"]

   :S [" ##"
       "## "]

   :Z ["## "
       " ##"]

   :O ["##"
       "##"]

   :I ["####"]})

(defn layout->tiles [layout color]
  (remove nil?
          (map-indexed (fn [y row]
                         (map-indexed (fn [x col]
                                        (if (= col \#)
                                          (tile/->Tile x y color)
                                          nil))
                                      row))
                       layout)))

(defn create [shape color]
  (let [layout (get layouts shape)]
    (layout->tiles layout color)))

(defrecord Tetromino [x y tiles]
  canvas/Renderable
  (render [this g]
    #_FIXME))
