(ns nyamaza.events
  (:require
   [re-frame.core :as re-frame]
   [nyamaza.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [com.degel.re-frame-firebase :as firebase]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
            (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 ::set-post
 (fn-traced [db [_ post]]
            (assoc db :post-text post)))

(re-frame/reg-event-db
 :set-user
 (fn-traced [db [_ user]]
            (assoc db :user user)))

(re-frame/reg-event-db
 :firebase-error
 (fn-traced [db [_ error]]
            (assoc db :firebase-error error)))

(re-frame/reg-event-fx
 ::publish-post
 (fn-traced [_ [_ post]]
            {:dispatch-n (list [::create-post-firestore post]
                               [::clear-post])}))

(re-frame/reg-event-db
 ::clear-post
 (fn-traced [db _]
            (dissoc db :post-text)))

(re-frame/reg-event-fx
 ::create-post-firestore
 (fn-traced [_ [_ post]]
            {:firestore/set {:path [:posts :post-1]
                             :data {:post-message post
                                    :time-and-date (js/Date.now)
                                    :answered false
                                    :creator "kevin"}
                             :on-success [::successful-post]
                             :on-failure #(js/console.log "Error! " %)}}))

(re-frame/reg-event-db
 ::successful-post
 (fn-traced [db _]
            (assoc db :is-post-successful? true )))

(re-frame/reg-event-db
 ::select-encryption
 (fn-traced [db [_ encryption]]
            (assoc db :set-encryption encryption)))

(re-frame/reg-event-db
 ::set-phone-number
 (fn-traced [db [_ phone-number]]
            (assoc db :phone-number phone-number)))

(re-frame/reg-event-db
 ::set-secret-message
 (fn-traced [db [_ secret-message]]
            (assoc db :secret-message secret-message)))

(re-frame/reg-event-db
 ::set-message
 (fn-traced [db [_ message]]
            (assoc db :message message)))
