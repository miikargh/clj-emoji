(ns emoji.emoji-test
  (:require [clojure.test :refer :all]
            [emoji.emoji :as emoji]))

(deftest group-start?-test
  (testing "return true if string contains group start with name"
    (is (every? true? (map emoji/group-start?
                           ["# group: this is a group start"
                            "# group: 123 this is also a group start"
                            "# group:----dasd02913 also a group start"]))))

  (testing "return false if string is not group start"
    (is (every? false? (map emoji/group-start?
                            ["# subgroup: this is not a group start"
                             "# derpgroup: 123 this also is not a group start"
                             "1F123 nope, still not a group start"])))))

(deftest get-group-name-test
  (testing "return correct group name"
    (is (= "Ice cream & Puppies"
           (emoji/get-group-name "# group: Ice cream & Puppies"))))

  (testing "return nil when the line is not a group start"
    (is (every? nil? (map emoji/get-group-name
                          ["# subgroup: Ice cream & Puppies"
                           "1F605     ; fully-qualified     # 😅 E0.6 grinning face with sweat"])))))

(deftest subgroup-start?-test
  (testing "return true if string contains subgroup start and name"
    (is (every? true? (map emoji/subgroup-start?
                           ["# subgroup: this is a subgroup start"
                            "# subgroup: 123 this is also a subgroup start"
                            "# subgroup:----dasd02913 also a subgroup start"]))))

  (testing "return false if string is not subgroup start"
    (is (every? false? (map emoji/subgroup-start?
                            ["# group: this is not a subgroup start"
                             "# derpgroup: 123 this also is not a subgroup start"
                             "1F123 nope, still not a subgroup start"])))))

(deftest get-subgroup-name-test
  (testing "return correct subgroup name"
    (is (= "Vanilla and Cockapoo"
           (emoji/get-subgroup-name "# subgroup: Vanilla and Cockapoo"))))

  (testing "return nil when the line is not a subgroup start"
    (is (every? nil? (map emoji/get-subgroup-name
                          ["# group: Ice cream & Puppies"
                           "1F605     ; fully-qualified     # 😅 E0.6 grinning face with sweat"])))))

(deftest emoji-line?-test
  (testing "return true if line contains emoji info"
    (is (every? true? (map emoji/emoji-line? ["1F600      ; fully-qualified     # 😀 E1.0 grinning face"
                                              "1F603      ; fully-qualified     # 😃 E0.6 grinning face with big eyes"
                                              "1F604      ; fully-qualified     # 😄 E0.6 grinning face with smiling eyes"
                                              "1F601      ; fully-qualified     # 😁 E0.6 beaming face with smiling eyes"
                                              "1F606      ; fully-qualified     # 😆 E0.6 grinning squinting face"
                                              "1F605      ; fully-qualified     # 😅 E0.6 grinning face with sweat"
                                              "1F923      ; fully-qualified     # 🤣 E3.0 rolling on the floor laughing"
                                              "1F602      ; fully-qualified     # 😂 E0.6 face with tears of joy"
                                              "1F642      ; fully-qualified     # 🙂 E1.0 slightly smiling face"
                                              "1F643      ; fully-qualified     # 🙃 E1.0 upside-down face"
                                              "1F609      ; fully-qualified     # 😉 E0.6 winking face"
                                              "1F60A      ; fully-qualified     # 😊 E0.6 smiling face with smiling eyes"
                                              "1F607      ; fully-qualified     # 😇 E1.0 smiling face with halo"]))))

  (testing "return false if line does not contain emoji info"
    (is (every?
         false?
         (map emoji/emoji-line?
              ["# emoji-test.txt"
               "# Date: 2020-01-21, 13:40:25 GMT"
               "# © 2020 Unicode®, Inc."
               "# Unicode and the Unicode Logo are registered trademarks of Unicode, Inc. in the U.S. and other countries."
               "# For terms of use, see http://www.unicode.org/terms_of_use.html"
               ""
               "# group: People & Body"
               " "
               "# subgroup: hand-fingers-open"
               "# Status Counts"
               "# fully-qualified : 3295"
               "# minimally-qualified : 614"
               "# unqualified : 250"
               "# component : 9"
               "#EOF"])))))

;; (deftest test-parse-emoji-line

;;   (testing "should return correct info"
;;     (is (=))))
