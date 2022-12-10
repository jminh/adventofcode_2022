(ns day10
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "noop
addx 3
addx -5
")

(defn parse-line [line]
  (if (= line "noop") [0]
      [0 0 (Integer. (get (re-matches #"(addx) (-?\d+)" line) 2))]))

(defn parse-line [line]
  (if (= line "noop") "noop"
      (Integer. (get (re-matches #"(addx) (-?\d+)" line) 2))))

(def example_input (->> "src/input/day10.txt"
                        slurp))

(def input (->> example_input
                str/split-lines
                (map parse-line)))

; {:move [] :pre_add false}
; use foo to generate each move

(defn foo [acc i]
  (if (:pre_add acc)
    (if (number? i)
      (-> acc
          (assoc :pre_add true)
          (update :move (partial apply conj) [[0 1] [i 1]]))
      (-> acc
          (assoc :pre_add false)
          (update :move conj [0 0])))
    (if (number? i)
      (-> acc
          (assoc :pre_add true)
          (update :move (partial apply conj) [[0 1] [0 1] [i 1]]))
      (-> acc
          (assoc :pre_add false)
          (update :move conj [0 1])))))

;(:move (reduce foo {:move [] :pre_ad false} [15, -11]))
;(:move (reduce foo {:move [] :pre_ad false} [15, -11, "noop"]))

(defn bar [[value clock-cycle target-cycle] i]
  (let [ret
        [(+ value (get i 0)) (+ clock-cycle (get i 1)) target-cycle]]
    (if (= target-cycle (get ret 1))
      (reduced (get ret 0))
      ret)))

(defn at-cycle [num, input]
  (reduce bar [1 0 num]
          (:move
           (reduce foo {:move [] :pre_add false} input))))

(+ (* 20 (at-cycle 20 input))
   (* 60 (at-cycle 60 input))
   (* 100 (at-cycle 100 input))
   (* 140 (at-cycle 140 input))
   (* 180 (at-cycle 180 input))
   (* 220 (at-cycle 220 input)))

; par1 13680
