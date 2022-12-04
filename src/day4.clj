(ns day4
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8")

(defn end-plus-one [range] [( nth range 0) (inc  (nth range 1))] )

(def ans1
  (->> example_input
       str/split-lines
       (map (partial re-matches #"(\d+)-(\d+),(\d+)-(\d+)"))
       (map rest)
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (map #(split-at 2 %))
       (map (fn [[a b]] [( end-plus-one a) (end-plus-one b)]))
       (map (fn [[a b]] [ (apply range a) (apply range b)]))
       (map (fn [[a b]] [(set a) (set b)]))
       (map (fn [[a b]] (or (clojure.set/subset? a b) (clojure.set/subset? b a) )))
       (filter identity)
       (count)
       ))

(def ans1
  (->> "src/input/day4.txt"
       slurp
       str/split-lines
       (map (partial re-matches #"(\d+)-(\d+),(\d+)-(\d+)"))
       (map rest)
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (map #(split-at 2 %))
       (map (fn [[a b]] [( end-plus-one a) (end-plus-one b)]))
       (map (fn [[a b]] [ (apply range a) (apply range b)]))
       (map (fn [[a b]] [(set a) (set b)]))
       (map (fn [[a b]] (or (clojure.set/subset? a b) (clojure.set/subset? b a) )))
       (filter identity)
       (count)
       ))

(prn ans1)

(def ans2
  (->> "src/input/day4.txt"
       slurp
       str/split-lines
       (map (partial re-matches #"(\d+)-(\d+),(\d+)-(\d+)"))
       (map rest)
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (map #(split-at 2 %))
       (map (fn [[a b]] [(end-plus-one a) (end-plus-one b)]))
       (map (fn [[a b]] [(apply range a) (apply range b)]))
       (map (fn [[a b]] [(set a) (set b)]))
       (map (fn [[a b]] (clojure.set/intersection a b)))
       (map empty?)
       (remove true?)
       (count)))

(prn ans2)
