(ns day11
  (:gen-class))

(require '[clojure.string :as str])

(defn divisible? [div num]
  (zero? (mod num div)))

(def monkey-state [[79 98]
                   [54 65 75 74]
                   [79 60 97]
                   [74]])

(def monkey-acc-state {:item monkey-state :acc(into [] (repeat (count monkey-state) 0))})

(def test-fn [#(if ((partial divisible? 23) %) 2 3)
               #(if ((partial divisible? 19) %) 2 0)
               #(if ((partial divisible? 13) %) 1 3)
               #(if ((partial divisible? 17) %) 0 1)])

(def operation [(partial * 19)
                (partial + 6)
                #(* % %)
                (partial + 3)])

; input by hand : monkey-state, monkey-acc-state, test-fn, operation {{{

(def monkey-state [[96 60 68 91 83 57 85]
                   [75 78 68 81 73 99]
                   [69 86 67 55 96 69 94 85]
                   [88 75 74 98 80]
                   [82]
                   [72 92 92]
                   [74 61]
                   [76 86 83 55]])

(def monkey-acc-state {:item monkey-state :acc(into [] (repeat (count monkey-state) 0))})

(def test-fn [#(if ((partial divisible? 17) %) 2 5)
              #(if ((partial divisible? 13) %) 7 4)
              #(if ((partial divisible? 19) %) 6 5)
              #(if ((partial divisible?  7) %) 7 1)
              #(if ((partial divisible? 11) %) 0 2)
              #(if ((partial divisible?  3) %) 6 3)
              #(if ((partial divisible?  2) %) 3 1)
              #(if ((partial divisible?  5) %) 4 0)])

(def operation [(partial * 2)
                (partial + 3)
                (partial + 6)
                (partial + 5)
                (partial + 8)
                (partial * 5)
                #(* % %)
                (partial + 4)])

; day1 input END }}}

(defn change-level [index item]
  (int (Math/floor (/ ((get operation index) item) 3))))

; index for monkey index, index=0 for monkey 0
(defn yield_monkey_item [state index]
  (for [item (get-in state [:item index])]
    (let [new-item (change-level index item)
          new-monkey-idx ((get test-fn index) new-item)]
      [index new-item new-monkey-idx])))

(defn move [state [index new-item new-monkey]]
  (let [{:keys [:acc :item]} state]
    (merge state
           {:acc (update acc index inc)}
           {:item (assoc item
                         index (subvec (get item index) 1)
                         new-monkey (conj (get item new-monkey) new-item) )})))

(reduce move monkey-acc-state (yield_monkey_item monkey-acc-state 0) )

; here cannot use iterate since we have to go through (range (count m_state))
(defn round [acc_state]
  (loop [monkey 0
         state acc_state]
    (if (= monkey (count (:item  acc_state))) state
        (recur (inc monkey)
               (reduce move state (yield_monkey_item state monkey))))))

(round monkey-acc-state)

(apply *
       (take 2
             (sort >
                   (:acc
                    (nth
                     (take 21
                           (iterate round monkey-acc-state)) 20)))))
; part1 sample
; [19 230 224 231 230 245 25 17]
; (* 245 231) = 56595

; --- --- ---  below no accumulator ---

(def monkey-state [[79 98]
                   [54 65 75 74]
                   [79 60 97]
                   [74]])

(def test-fn [#(if ((partial divisible? 23) %) 2 3)
              #(if ((partial divisible? 19) %) 2 0)
              #(if ((partial divisible? 13) %) 1 3)
              #(if ((partial divisible? 17) %) 0 1)])

(def operation [(partial * 19)
                (partial + 6)
                #(* % %)
                (partial + 3)])

(defn change-level [index item]
  (int (Math/floor (/ ((get operation index) item) 3))))

; index for monkey index, index=0 for monkey 0
(defn yield_monkey_item [monkey-state index]
  (for [item (get monkey-state index)]
    (let [new-item (change-level index item)
          new-monkey ((get test-fn index) new-item)]
      [index new-item new-monkey])))

(defn move [state [index new-item new-monkey]]
  (assoc state
         index (subvec (get state index) 1)
         new-monkey (conj (get state new-monkey) new-item)))

(reduce move monkey-state (yield_monkey_item monkey-state 0))

; here cannot use iterate since we have to go through (range (count m_state))
(defn round [m_state]
  (loop [monkey 0
         state m_state]
    (if (= monkey (count m_state)) state
        (recur (inc monkey)
               (reduce move state (yield_monkey_item state monkey))))))

(round monkey-state)

(take 11
      (iterate round monkey-state))

(->> (take 21 (iterate round monkey-state))
     (map (fn [lst] (mapv count lst)))
     (apply mapv vector)
     (map #(apply + %)))
