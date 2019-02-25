(ns mine-sweep.ui.auth.subs-test
  (:require-macros [cljs.test :refer [deftest testing is]])
  (:require [mine-sweep.ui.auth.subs :refer [format-auth-form-errors]]))

(deftest test-format-auth-form-errors
  (testing "test with input errors is nil"
    (is (= {:http-status nil
            :username    nil
            :password    nil}
           (format-auth-form-errors nil))))
  (testing "test with input http-status error 400 or 401"
    (is (= {:http-status "Wrong username or password"
            :username    nil
            :password    nil}
           (format-auth-form-errors {:http-status 400}))
        (= {:http-status "Wrong username or password"
            :username    nil
            :password    nil}
           (format-auth-form-errors {:http-status 401}))))
  (testing "test with input http-status is nil"
    (is (= {:http-status nil
            :username    nil
            :password    nil}
           (format-auth-form-errors {:http-status nil}))))
  (testing "test with input username error"
    (is (= {:http-status nil
            :username "username can't be blank\n"
            :password nil}
           (format-auth-form-errors {:username ["can't be blank"]}))))
  (testing "test with input username and pasword error"
    (is (= {:http-status nil
            :username "username error\n"
            :password "password is too short\npassword invalid\n"}
           (format-auth-form-errors {:username ["error"]
                                     :password ["is too short", "invalid"]})))))
