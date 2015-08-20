(ns ^:figwheel-always tetris.tetromino
    (:require (tetris.lib.canvas :as canvas)
              (tetris.tile :as tile)))

(defrecord Tetromino [tiles x y]
  canvas/Renderable
  (render [this g]
    (canvas/save! g)
    (canvas/translate! g x y)
    (doseq [t tiles] (canvas/render t g))
    (canvas/restore! g)))

(defn tiles [{:keys [layout color]}]
  (->> layout
       (map-indexed (fn [y row]
                         (map-indexed (fn [x col]
                                        (if (= col \#)
                                          (tile/->Tile x y color)
                                          nil))
                                      row)))
       (remove nil?)))

(def tetrominoes

  (map #(->Tetromino (tiles %) nil nil)

       [{:layout ["###"
                  " # "]
         :color "lime"}

        {:layout [" #"
                  " #"
                  "##"]
         :color "yellow"}

        {:layout ["# "
                  "# "
                  "##"]
         :color "magenta"}

        {:layout [" ##"
                  "## "]
         :color "cyan"}

        {:layout ["## "
                  " ##"]
         :color "orange"}

        {:layout ["##"
                  "##"]
         :color "blue"}

        {:layout ["####"]
         :color "red"}]))

(defn random []
  (rand-nth tetrominoes))
