(ns day9
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2")

(def example_input (->> "src/input/day9.txt"
                        slurp))

; head, tail starting point (0, 0)

(defn parse-line [line]
  (let [[_ direction num]
        (re-matches #"([UDLR]) (\d+)" line)]
    [direction (Integer. num)]))

(defn parse-input [input]
  (->> input
       str/split-lines
       (map parse-line)))

(defn move-up [x y] [x [inc y]])
(defn move-down [x y] [x [dec y]])
(defn move-left [x y] [(dec x) y])
(defn move-right [x y] [(inc  x) y])

(defn move [[x y] [direction num]]
  (case direction
    "U" [x (+ y num)]
    "D" [x (- y num)]
    "L" [(- x num) y]
    "R" [(+ x num) y]))

(defn move-one-by-one [[direction num]]
  (repeat num [direction 1]))

(def head-positions
  (->> example_input
       parse-input
       (map move-one-by-one)
       (apply concat)
       (reductions move [0,0])))

(defn touch? [[x_t y_t] [x_h y_h]]
  (and (<= (Math/abs (- x_t x_h)) 1)
       (<= (Math/abs (- y_t y_h)) 1)))

; diagonally next
(defn tail-next [[x y]]
  [[(inc x) (inc y)] [(inc x) (dec y)]
   [(dec x) (dec y)] [(dec x) (inc y)]])

(defn move-tail [[x_t y_t] [x_h y_h]]
  (if (touch? [x_t y_t] [x_h y_h])
    [x_t y_t]
    (cond
      (= x_t x_h) (if (> y_h y_t) [x_t (inc y_t)] [x_t (dec y_t)])
      (= y_t y_h) (if (> x_h x_t) [(inc x_t) y_t] [(dec x_t) y_t])
      :else (some #(if (touch? % [x_h y_h]) %) (tail-next [x_t y_t])))))

(prn
 (count
  (set
   (reductions move-tail [0 0] head-positions))))
; answer1 6243
