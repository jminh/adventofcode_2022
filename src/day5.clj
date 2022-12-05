(ns day5
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "
    [D]
[N] [C]
[Z] [M] [P]
 1   2   3
")

(def supply-stack [nil,
                   [\Z \N],
                   [\M \C \D],
                   [\P]])

(def example_input "move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
")

(def example_input (str/split-lines example_input))

(def supply-stack [nil,
                   [\F \T \C \L \R \P \G \Q],
                   [\N \Q \H \W \R \F \S \J],
                   [\F \B \H \W \P \M \Q],
                   [\V \S \T \D \F],
                   [\Q \L \D \W \V \F \Z],
                   [\Z \C \L \S],
                   [\Z \B \M \V \D \F],
                   [\T \J \B],
                   [\Q \N \B \G \L \S \P \H]])

(def example_input (->> "src/input/day5.txt"
                        slurp
                        str/split-lines
                        (drop 10)))

(defn move [state, [from ,to]]
  (let [from-item (peek (get state from))
        s (assoc-in state [from] (pop (get state from)))]
    (update-in s [to] conj from-item)))

(move supply-stack [2 1])

(def ans1
  (->> example_input
       (map (partial re-matches #"move (\d+) from (\d+)\sto\s+(\d+)"))
       (map (partial rest))
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
;       (map #(repeat (first %) (rest %)))
       (mapcat #(repeat (first %) (rest %)))
       (reduce move supply-stack)
       (drop 1)
       (map #(peek %))
       ))

(prn ans1) ; VGBBJCRMN

(defn move2 [state, [num, from, to]]
  (let [from-count (count (get state from))
        from-items (subvec (get state from) (- from-count num))
        s (assoc-in state [from] (subvec (get state from) 0 (- from-count num)))]
    (update-in s [to] (partial apply conj) from-items)))

(def ans2
  (->> example_input
       (map (partial re-matches #"move (\d+) from (\d+)\sto\s+(\d+)"))
       (map (partial rest))
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (reduce move2 supply-stack)
       (drop 1)
       (map #(peek %))))

(prn ans2) ; LBBVJBRMH
