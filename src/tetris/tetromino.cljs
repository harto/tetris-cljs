(ns ^:figwheel-always tetris.tetromino
    (:require (tetris.lib.canvas :as canvas)
              (tetris.tile :as tile))
    (:use (tetris.core :only (col->x cols row->y))))

(defn- count-tiles [tetromino dimension]
  (->> (:tiles tetromino)
       (map dimension)
       (apply max)
       (inc)))

(defn width [tetromino]
  (count-tiles tetromino :col))

(defn height [tetromino]
  (count-tiles tetromino :row))

(defn render-bounding-box [tetromino g]
  (canvas/set-properties! g {"strokeStyle" "#000"})
  (let [size (max (width tetromino) (height tetromino))]
    (canvas/stroke-rect! g 0 0 (col->x size) (row->y size))))

(defrecord Tetromino [tiles col row]
  canvas/Renderable
  (render [this g]
    (canvas/save! g)
    (canvas/translate! g (col->x col) (row->y row))
    (doseq [t tiles]
      (canvas/render t g))
    (render-bounding-box this g)
    (canvas/restore! g)))

(defn rotate [tetromino]
  (let [multiplier 1
        offset (-> (max (width tetromino) (height tetromino))
                   (dec)
                   (/ 2))]
    (assoc tetromino :tiles (for [{:keys [col row] :as t} (:tiles tetromino)]
                              (assoc t
                                     :col (-> row
                                              (- offset)
                                              (* multiplier)
                                              (+ offset))
                                     :row (-> col
                                              (- offset)
                                              (* (- multiplier))
                                              (+ offset)))))))

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

       [{:layout ["   "
                  "###"
                  " # "]
         :color "lime"}

        {:layout ["   "
                  "###"
                  "  #"]
         :color "yellow"}

        {:layout ["   "
                  "###"
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

        {:layout ["    "
                  "####"]
         :color "red"}]))

(defn random []
  (let [t (rand-nth tetrominoes)]
    (assoc t :col (quot (- cols (width t)) 2) :row 5)))
