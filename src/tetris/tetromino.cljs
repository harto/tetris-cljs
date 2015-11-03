(ns ^:figwheel-always tetris.tetromino
    (:require (tetris.core :as tetris)
              (tetris.lib.canvas :as canvas)
              (tetris.tile :as tile))
    (:use (tetris.core :only (col->x row->y))))

(defn col [{:keys [col tiles]}]
  (+ col (apply min (map :col tiles))))

(defn row [{:keys [row tiles]}]
  (+ row (apply min (map :row tiles))))

(defn width [{:keys [tiles]}]
  (let [tile-cols (map :col tiles)]
    (inc (- (apply max tile-cols)
            (apply min tile-cols)))))

(defn height [{:keys [tiles]}]
  (let [tile-rows (map :row tiles)]
    (inc (- (apply max tile-rows)
            (apply min tile-rows)))))

(defn in-bounds? [tetromino]
  (let [c (col tetromino)
        r (row tetromino)
        w (width tetromino)
        h (height tetromino)]
    (and (<= 0 c)
         (<= 0 r)
         (<= (+ c w) tetris/cols)
         (<= (+ r h) tetris/rows))))

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

(defn move-left [tetromino]
  (update-in tetromino [:col] dec))

(defn move-right [tetromino]
  (update-in tetromino [:col] inc))

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
    (assoc t :col (quot (- tetris/cols (width t)) 2) :row 5)))
