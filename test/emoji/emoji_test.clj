(ns emoji.emoji-test
  (:require [clojure.test :refer :all]
            [emoji.emoji :as emoji]))

(deftest group-start?-test
  (testing "return true if string starts with '# group:'"
    (is (every? true? (map emoji/group-start?
                           ["# group: this is a group start"
                            "# group: 123 this is also a group start"
                            "# group:----dasd02913 also a group start"]))))

  (testing "return false if string does not start with '# group:'"
    (is (every? false? (map emoji/group-start?
                           ["# subgroup: this is not a group start"
                            "# derpgroup: 123 this also is not a group start"
                            "1F123 nope, still not a group start"])))))

(every? true? [true true true])
