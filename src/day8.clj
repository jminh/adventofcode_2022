(ns day8
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "30373
25512
65332
33549
35390")

(def example_input (->> "src/input/day8.txt"
                        slurp))

(def input
  (->> example_input
       str/split-lines
       (map seq)
       (mapv (fn [nums] (mapv #(Integer/parseInt (str %)) nums)))))

; // refactor the name pull -> transpose
(defn pull [grid]
  (apply mapv vector grid))

(defn visible_inside? [idx, array]
  (let [before-max (apply max (subvec array 0 idx))
        after-max (apply max  (subvec array (inc idx)))
        value (get array idx)]
    (if (< before-max value)
      true
      (if (< after-max value)
        true))))

(defn visible? [[x y], grid]
  (let [row_num (count grid)
        col_num (count (first grid))]
    (cond
      (or (= x 0) (= y 0)) true
      (or (= x (dec row_num)) (= y (dec col_num))) true
      :else (or (visible_inside? y (get grid x))
                (visible_inside? x (get (pull grid) y))))))

(prn
 (count
  (remove nil?
          (for [i (range (count input))
                j (range (count (first input)))]
            (visible? [i j] input)))))
; answer 1 1779

(defn foo [[acc value] item]
  (cond
    (< item value) [(inc acc) value]
    (= item value) (reduced [(inc acc) value])
    :else (reduced [acc value])))

(defn distance [value array]
  (get (reduce foo [0 value] array) 0))

(defn score [[x y] grid]
  (let [value (get-in grid [x y])
        row (get grid x)
        before (subvec row 0 y)
        after (subvec row (inc y))]
    (* (distance value (rseq before))
       (distance value after))))

(defn total-score [[x y] grid]
  (* (score [x y] grid)
     (score [y x] (pull grid))))

(prn
 (apply max
        (for [i (range (count input))
              j (range (count (first input)))]
          (total-score [i j] input))))
; answer2 17224

(for [i (range (count input))
      j (range (count (first input)))]
  [i j])
