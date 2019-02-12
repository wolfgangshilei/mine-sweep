(ns mine-sweep.ui.record.events
  (:require [re-frame.core :as rf]
            [mine-sweep.utils.re-frame :refer [register-event-db
                                               register-event-fx]]
            [mine-sweep.api.session :as session-api]
            [mine-sweep.config :as config]
            [mine-sweep.utils.log :refer [log]]))

(defn store-records
  [db [_ records {:keys [username order-by]}]]
  (assoc-in db [:ui.record/records username order-by] records))

(register-event-db
 ::store-records
 store-records)

(register-event-fx
 :ui.record/get-records
 (fn [{db :db} [_ params]]
   {:db                  db
    ::server-get-records params}))

(rf/reg-fx
 ::server-get-records
 (fn [params]
   (session-api/get-records params
                            #(rf/dispatch [::store-records (:data %) params])
                            nil)))

(register-event-fx
 :ui.record/create-record
 (fn [{db :db} [_ record-time level]]
   {:db db
    ::server-create-record {:record record-time
                            :level  level}}))

(defn- update-my-stored-records
  [db [_ record]]
    (let [username (-> db :ui.auth/session :username)
          level    (-> record :level keyword)]
      (-> db
          ;; add new record in the best list, sort by record and take first n.
          (update-in [:ui.record/records username :best level]
                     #(as-> % rs
                        (conj rs record)
                        (sort-by :record rs)
                        (take config/record-num rs)
                        (vec rs)))

          ;; add new record in the latest list at the beginning, and take first n.
          (update-in [:ui.record/records username :latest level]
                     #(->> %
                       (into [record])
                       (take config/record-num)
                       vec)))))

(comment (update-my-stored-records
          {:ui.auth/session {:username "a"}
           :ui.record/records {"a" {:best {:easy [{:inserted_at "2019-02-12T14:40:41" :level "easy" :record 78}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 127}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 137}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 147}
                                                  {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 157}]}
                                    :latest {}}}}
          [nil {:inserted_at "2019-02-12T14:40:41" :level "easy" :record 100}]))

(register-event-db
 ::update-my-stored-records
 update-my-stored-records)

(rf/reg-fx
 ::server-create-record
 (fn [params]
   (session-api/new-record
    params
    #(rf/dispatch [::update-my-stored-records (:data %)])
    nil)))

(comment (session-api/new-record
          {:record 188 :level :easy}
          nil nil))
