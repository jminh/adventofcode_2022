(ns day3
  (:gen-class))

(require '[clojure.string :as str])

(defn char-range [start end]
  (map char (range (int start) (inc (int end)))))

(def char-num-map (merge
                   (zipmap (char-range \a \z) (range 1 27))
                   (zipmap (char-range \A \Z) (range 27 53))))

(def raw_input "vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw")

(def ans1
  (->> raw_input
       str/split-lines
       (map #(split-at (/ (count %) 2) %))
       (map (fn [[a b]] [(set a) (set b)]))
       (map (fn [[a b]] (clojure.set/intersection a b)))
       (map vec)
       (flatten)
       (map char-num-map)
       (apply +)))

(def ans1
  (->> "src/input/day3.txt"
       slurp
       str/split-lines
       (map #(split-at (/ (count %) 2) %))
       (map (fn [[a b]] [(set a) (set b)]))
       (map (fn [[a b]] (clojure.set/intersection a b)))
       (map vec)
       (flatten)
       (map char-num-map)
       (apply +)))

(prn ans1)
