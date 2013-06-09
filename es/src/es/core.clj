(ns es.core
  (:use [clojure.pprint]))

(defn prep-file []
  (def words-file  (into [] (clojure.string/split-lines (slurp "/home/shlomiv/ginger/rnd/es-vs-luc/es/words.txt"))))
  (def max (count words-file))

  (def five-grams (partition 5 (repeatedly #(rand-int max) )))

  (defn generate-grams []
    (map (fn [line] (str (clojure.string/join " " (map words-file line)) "\t" (rand-int max)) )
         five-grams))

  (spit "shlomi1.txt" (str (clojure.string/join "\n" (take 1000000 (generate-grams))) "\n")))

