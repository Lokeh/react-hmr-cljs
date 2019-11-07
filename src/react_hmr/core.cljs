(ns react-hmr.core
  (:require ["react-dom" :as react-dom]
            ["react" :as react]
            [react-hmr.react :refer [$]]
            [react-hmr.refresh :as refresh :refer [defnc]]
            [react-hmr.dep-b :as b]
            [react-hmr.dep-z :as z]))


;;
;; Using the Hot Reloadable `defnc` macro
;;

(defnc my-refreshed-component
  [props]
  (let [[count set-count] (react/useState 0)
        [name set-name] (react/useState "React Refresh")]
    ;; kept it's state!
    ($ react/Fragment
       ($ :div {:style {:background "pink"}}
          ($ :div (b/greet name))
          ($ :div ($ :button {:onClick #(set-count inc)} "+ " count))
          ($ :div ($ :input {:type "text"
                             :value name
                             :onChange #(set-name (.. % -target -value))})))
       ($ :br)
       ($ z/component {:name "child in another file"})
       ($ :br)
       ($ z/memo-component {:name "memoized child in another file"}))))


;;
;;
;; Editing this file will automatically hot-load your code in the browser
;;
;; Try changing the state of the "refreshed" component, then touch the file to
;; cause a reload.
;;
;;


(defn start []
  (react-dom/render ($ my-refreshed-component)
                    (. js/document getElementById "app")))


(defn ^:dev/after-load reload []
  (refresh/reload))
