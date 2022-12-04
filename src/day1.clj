(ns day1
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "1000
2000
3000

4000

5000
6000

7000
8000
9000

10000")

(def ans1
  (->> example_input
       str/split-lines
       (partition-by (partial = ""))
       (remove (partial = '("")))
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (map (partial apply +))
       (apply max)))

(def ans1
  (->> "src/input/day1.txt"
       slurp
       str/split-lines
       (partition-by (partial = ""))
       (remove (partial = '("")))
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (map (partial apply +))
       (apply max)))

(prn ans1)

(def ans2
  (->> "src/input/day1.txt"
       slurp
       str/split-lines
       (partition-by (partial = ""))
       (remove (partial = '("")))
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (map (partial apply +))
       (sort >)
       (take 3)
       (apply +)))

(prn ans2)
