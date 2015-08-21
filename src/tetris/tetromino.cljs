(ns ^:figwheel-always tetris.tetromino
    (:require (tetris.lib.canvas :as canvas)
              (tetris.tile :as tile))
    (:use (tetris.core :only (col->x cols row->y))))

(defrecord Tetromino [tiles col row]
  canvas/Renderable
  (render [this g]
    (canvas/save! g)
    (canvas/translate! g (col->x col) (row->y row))
    (doseq [t tiles] (canvas/render t g))
    (canvas/restore! g)))

(defn extent [tetromino dimension]
  (->> (:tiles tetromino)
       (map dimension)
       (apply max)
       (inc)))

(defn width [tetromino]
  (extent tetromino :col))

(defn height [tetromino]
  (extent tetromino :row))

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
  (let [t (rand-nth tetrominoes)]
    (assoc t :col (quot (- cols (width t)) 2) :row 0)))
