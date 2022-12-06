(ns day6
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "mjqjpqmgbljsphdztnvjfqwrcgsmlb")

(def input (->> "src/input/day6.txt"
                slurp
                (str/trim-newline)))

(def ans1 (->> input
               (partition 4 1)
               (map set)
               (map count)
               (map-indexed vector)
               (some (fn [[i n]] (if (= n 4) (+ i 4))))))

(prn ans1) ; 1896

(def ans2 (->> input
               (partition 14 1)
               (map set)
               (map count)
               (map-indexed vector)
               (some (fn [[i n]] (if (= n 14) (+ i 14))))))
(prn ans2) ; 3452
