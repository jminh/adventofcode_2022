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
; 1779

; --

(for [i (range (count input))
      j (range (count (first input)))]
  [i j])
