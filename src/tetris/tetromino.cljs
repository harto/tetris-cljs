(ns ^:figwheel-always tetris.tetromino
    (:require (tetris.lib.canvas :as canvas)
              (tetris.tile :as tile))
    (:use (tetris.core :only (col->x row->y))))

(defrecord Tetromino [tiles col row]
  canvas/Renderable
  (render [this g]
    (canvas/save! g)
    (canvas/translate! g (col->x col) (row->y row))
    (doseq [t tiles] (canvas/render t g))
    (canvas/restore! g)))

(defn tiles [{:keys [layout color]}]
  (->> layout
       (map-indexed (fn [row cs]
                         (map-indexed (fn [col c]
                                        (if (= c \#)
                                          (tile/->Tile col row color)
                                          nil))
                                      cs)))
       (apply concat)
       (remove nil?)))

(def tetrominoes

  (map #(->Tetromino (tiles %) nil nil)

       [{:layout ["###"
                  " # "]
         :color "lime"}

        {:layout ["###"
                  "  #"]
         :color "yellow"}

        {:layout ["###"
                  "#  "]
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
