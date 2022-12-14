(ns day12
  (:gen-class))

(require '[clojure.string :as str])

(defn neighbors [[i j]] [[i (inc j)] [i (dec j)] [(dec i) j] [(inc i) j]])

(defn candidates [graph [i j]]
  (->> (neighbors [i j])
       (filter #(if (< (compare (get-in graph % "~") (get-in graph [i j])) 2) true))))

(def example_input "Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi")

(def input
  (->> example_input
       str/split-lines
       (mapv #(str/split % #""))))

(def input
  (->> "src/input/day12.txt"
       slurp
       str/split-lines
       (mapv #(str/split % #""))))

(defn search-index [grid search]
  (loop [i 0 j 0]
    (if (= search (get-in grid [i j]))
      [i j]
      (if (< i (count grid))
        (if (< j (count (first grid)))
          (recur i (inc j))
          (recur (inc i) 0))))))

(comment
  ; for search-index, alternatively you can use below code
  (for [[i row] (map-indexed vector input)
        [j v] (map-indexed vector row)
        :when (= "E" (get-in input [i j]))]
    [i, j]))

(def start (search-index input "S"))
(def end (search-index input "E"))

(def graph (-> input
               (assoc-in start "a")
               (assoc-in end "z")))

; below bfs is wrong
(defn bfs [graph s e]
  (loop [branches (conj (clojure.lang.PersistentQueue/EMPTY) s)
         visit #{}
         step 0]
    (if (empty? branches)
      false
      (let [node (peek branches)
            remaining-branches (pop branches)]
        (or (if (= e node) step)
            (recur (into remaining-branches (remove #(visit %) (candidates graph node)))
                   (conj visit node)
                   (inc step)))))))
;(bfs graph start end)

; before work out the bfs, use below code {{{
; http://www.loganlinn.com/blog/2013/04/22/dijkstras-algorithm-in-clojure/

(def ^:private inf Double/POSITIVE_INFINITY)

(defn update-costs
  [g costs unvisited curr]
  (let [curr-cost (get costs curr)]
    (reduce-kv
     (fn [c nbr nbr-cost]
       (if (unvisited nbr)
         (update-in c [nbr] min (+ curr-cost nbr-cost))
         c))
     costs
     (get g curr))))

(defn dijkstra
  ([g src]
   (dijkstra g src nil))
  ([g src dst]
   (loop [costs (assoc (zipmap (keys g) (repeat inf)) src 0)
          curr src
          unvisited (disj (apply hash-set (keys g)) src)]
     (cond
       (= curr dst)
       (select-keys costs [dst])

       (or (empty? unvisited) (= inf (get costs curr)))
       costs

       :else
       (let [next-costs (update-costs g costs unvisited curr)
             next-node (apply min-key next-costs unvisited)]
         (recur next-costs next-node (disj unvisited next-node)))))))
; }}}

(def new-graph
  (apply merge-with into
         (for [[i row] (map-indexed vector graph)
               [j v] (map-indexed vector row)
               candidate (candidates graph [i j])]
           {[i j] {candidate 1}})))

(dijkstra new-graph start end)
; part1 answer 425

(defn search-all-index [grid search]
  "note: you can use for :when approch as above search-index"
  (loop [i 0 j 0
         ret []]
    (if (< i (count grid))
      (if (< j (count (first grid)))
        (recur i (inc j)
               (if (= search (get-in grid [i j])) (conj ret [i j]) ret))
        (recur (inc i) 0
               (if (= search (get-in grid [i j])) (conj ret [i j]) ret)))
      ret)))

(->> (search-all-index graph "a")
     (map #(dijkstra new-graph % end))
     (map #(get % end))
     (apply min))
; part2 answer 418

(comment
(compare "a" "e")
(compare "c" "a")
(compare "~" "a")
(compare "~" "z")

(candidates graph [0 0])
(candidates graph [0 2])
(candidates graph [1 2])

;; https://clojuredocs.org/clojure.string/split
;; to get back all the characters of a string, as a vector of strings:
;; user=> (str/split " q1w2 " #"")
;; [" " "q" "1" "w" "2" " "]
;; Note: sequence, in contrast, would return characters.
;; (seq " q1w2 ")
  )
