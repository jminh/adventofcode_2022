(ns day7
  (:gen-class))

(require '[clojure.string :as str])

(def example_input "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k")

(def example_input (->> "src/input/day7.txt"
                        slurp))

(defn parse_cd [line]
  (last (str/split line #"\s+")))

(defn parse_ls [line]
  (let [[x y] (str/split line #"\s+")]
    (if (= x "dir") [y x]
        [y (. Integer parseInt x)])))

(defn tokenMap [line]
  (cond
    (str/starts-with? line "$ cd") [:cd (parse_cd line)]
    (str/starts-with? line "$ ls") nil
    :else (parse_ls line)))

(def tokens
  (->> example_input
       str/split-lines
       (map tokenMap)
       (remove nil?)))

; [:cd "/"]
; ["a", "dir"]
; ["b.txt", 14848514]
(defn foo2 [m [a b]]
  (if (= :cd a)
    (if (= b "..") (update m :cwd pop)
        (update m :cwd conj b))
    (update-in m [(str/join "/" (:cwd m))] conj (if (= b "dir") (str (str/join "/" (:cwd m)) "/" a) b))))

; parse-tokens is the desired data structure from input!
(def parse-tokens
  (reduce foo2 {:cwd []} tokens))

(declare size)

(defn bar [acc i]
  (if (string? i)
    (+ (size i) acc)
    (+ i acc)))

(defn size [folder]
  (reduce bar 0 (get parse-tokens folder)))

(size "/")
(size "//d")
(size "//a")
(size "//a/e")

; asn1
(prn
 (apply +
        (filter #(< % 100000)
                (map size
                     (keys (dissoc parse-tokens :cwd))))))
;print 1642503

(def unused-space (- 70000000 (size "/")))
(def need-space (- 30000000 unused-space))

(prn
 (apply min
        (filter #(> % need-space)
                (map size
                     (keys (dissoc parse-tokens :cwd))))))
;print 6999588

; --- in parse-tokens use foo instead foo2 and see how ouptut varies

(defn foo [m [a b]]
  (if (= :cd a)
    (update m :cwd conj b)
    m))

(defn foo [m [a b]]
  (if (= :cd a)
    (update m :cwd conj b)
    (assoc-in m [(str/join "/" (:cwd m)) a] b)))

(defn foo [m [a b]]
  (if (= :cd a)
    (if (= b "..") (update m :cwd pop)
        (update m :cwd conj b))
    (assoc-in m [(str/join "/" (:cwd m)) (str (str/join "/" (:cwd m)) "/" a)] b)))

(defn foo [m [a b]]
  (if (= :cd a)
    (if (= b "..") (update m :cwd pop)
        (update m :cwd conj b))
    (assoc-in m [(str/join "/" (:cwd m)) (str (str/join "/" (:cwd m)) "/" a)] b)))
