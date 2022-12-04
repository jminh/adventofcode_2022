(ns day2
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "A Y
B X
C Z
")

(def score-map {["A", "X"] (+ 3 1)
                ["A", "Y"] (+ 6 2)
                ["A", "Z"] (+ 0 3)
                ["B", "X"] (+ 0 1)
                ["B", "Y"] (+ 3 2)
                ["B", "Z"] (+ 6 3)
                ["C", "X"] (+ 6 1)
                ["C", "Y"] (+ 0 2)
                ["C", "Z"] (+ 3 3)})

(def ans1
  (->> example_input
       str/split-lines
       (map #(str/split % #" "))
       (map score-map)
       (apply +)))

(def ans1
  (->> "src/input/day2.txt"
       slurp
       str/split-lines
       (map #(str/split % #" "))
       (map score-map)
       (apply +)))

(prn ans1)

(def score-map {["A", "X"] (+ 0 3)
                ["A", "Y"] (+ 3 1)
                ["A", "Z"] (+ 6 2)
                ["B", "X"] (+ 0 1)
                ["B", "Y"] (+ 3 2)
                ["B", "Z"] (+ 6 3)
                ["C", "X"] (+ 0 2)
                ["C", "Y"] (+ 3 3)
                ["C", "Z"] (+ 6 1)})

(def ans2
  (->> "src/input/day2.txt"
       slurp
       str/split-lines
       (map #(str/split % #" "))
       (map score-map)
       (apply +)))

(prn ans2)
