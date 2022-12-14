(ns day13
  (:gen-class))

(require '[clojure.string :as str])

(defn is-arrary-opening? [array] (= \[ (first array)))
(defn is-arrary-ending? [array] (= \] (first array)))

(defn is-digit? [array]
  (and (>= (int (first array)) (int \0))
       (<= (int (first array)) (int \9))))

(defn non-digit? [arrary] (not (is-digit? arrary)))

(defn char-to-number [character]   (- (int character) (int \0)))

(defn parse-number [array number]
  (if (non-digit? array)
    [number array]
    (recur (rest array)
           (+ (char-to-number (first array)) (* number 10)))))

(declare parse-array)
(defn parse-value [array]
  (cond
    (is-digit? array) (parse-number array 0)
    (is-arrary-opening? array) (parse-array (rest array) [])
    :else (println "Incoorect character?")))

(defn drop-comma-if-first [array]
  (if (= \, (first array))
    (rest array)
    array))

(defn parse-array [text array]
  (if (is-arrary-ending? text)
    [array (rest text)]
    (let [value-result (parse-value (drop-comma-if-first text))
          value (nth value-result 0)
          rest-text (nth value-result 1)]
      (recur rest-text (conj array value)))))

(defn parse [text]
  (nth (parse-value text) 0))

(parse "[0]")
(parse "[[1,2],0]")
