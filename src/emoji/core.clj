(ns emoji.core
  (:require [clojure.string :as string]
            [clojure.data.json :as json]))

(defn fetch-emoji-txt
  "Slurp emoji-txt if path gicen. Otherwise fetch emoji specification text file from the web."
  ([]
   (slurp "https://unicode.org/Public/emoji/13.0/emoji-test.txt"))
  ([path]
   (slurp path)))

(defn slurp-emoji-txt
  []
  (fetch-emoji-txt "/home/miikargh/dev/emoji/emoji-test.txt"))

(defn group-start?
  "Is the given line start of a group"
  [line]
  (string/starts-with? line "# group:"))

(defn get-group-name
  "Returns the group name if string is a group start."
  [line]
  (when (group-start? line)
    (string/trim (last (string/split line #"group:")))))

(defn subgroup-start?
  "Is the given line start of a group"
  [line]
  (string/starts-with? line "# subgroup:"))

(defn get-subgroup-name
  "Returns the subgroup name if string is a subgroup start."
  [line]
  (when (subgroup-start? line)
    (string/trim (last (string/split line #"subgroup:")))))

(defn emoji-line? [line]
  (not (or (string/starts-with? line "#")
           (empty? (string/trim line)))))

(defn parse-emoji-line
  [emoji-line]
  (let [[codes-raw other] (string/split emoji-line #";")
        other-splitted (filter #(not (empty? %))
                               (string/split other #"\s"))
        emoji (nth other-splitted 2)
        name (string/join " " (take-last 2 other-splitted))
        codes (filter #(not (empty? %)) (string/split codes-raw #"\s"))]
    {:char emoji :codes codes :name name}))

(defn parse-emojis
  ([emoji-text]
   (let [lines (string/split emoji-text #"\n")]
     (parse-emojis lines nil nil [])))
  ([lines group subgroup parsed]
   (println (count lines))
   (if (empty? lines)
     parsed
     (let [line (first lines)
           new-group (get-group-name line)
           new-subgroup (get-subgroup-name line)]
       (if (or new-group new-subgroup)
         (recur (rest lines) (or new-group group) (or new-subgroup subgroup) parsed)
         (if (emoji-line? line)
           (let [emoji (parse-emoji-line line)
                 emoji-with-groups (assoc emoji :group group :subgroup subgroup)]
             (recur (rest lines) group subgroup (conj parsed emoji-with-groups)))
           (recur (rest lines) group subgroup parsed)))))))

(defn write-emoji-json
  "Writes emoji json from given emoji-map"
  ([path]
   (write-emoji-js
    on path (parse-emojis (slurp-emoji-txt)))) ;; TODO: Change slurp to fetch
  ([path emoji-map]
   (spit path (json/write-str emoji-map))))

(write-emoji-json "./emoji.json")

(spit "./sneg.json" (json/write-str {:kek "slperh"}))

(assoc {:derp "gerrp"} :zurp 2 :snarp 3)
