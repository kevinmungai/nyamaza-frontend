(ns nyamaza.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [nyamaza.events :as events]
   [nyamaza.routes :as routes]
   [nyamaza.views :as views]
   [nyamaza.config :as config]
   [nyamaza.firebase :as firebase-config]
   [com.degel.re-frame-firebase :as firebase]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (firebase/init :firebase-app-info      firebase-config/firebase-app-info
                                        ; See: https://firebase.google.com/docs/reference/js/firebase.firestore.Settings
                 :firestore-settings     {:timestampsInSnapshots true}
                 :get-user-sub           [:user]
                 :set-user-event         [:set-user]
                 :default-error-handler  [:firebase-error])
  (mount-root))
