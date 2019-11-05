(ns react-hmr.core
  (:require ["react" :as react]
            ["react-dom" :as react-dom]
            [react-hmr.fresh :as fresh :refer [defnc]]))


(defn $
  [type & args]
  (let [type' (if (keyword? type)
                (name type)
                type)]
    (if (map? (first args))
      (apply react/createElement type' (clj->js (first args)) (rest args))
      (apply react/createElement type' nil args))))


;; Make printing React elements work
(when (exists? js/Symbol)
  (extend-protocol IPrintWithWriter
    js/Symbol
    (-pr-writer [sym writer _]
      (-write writer (str "\"" (.toString sym) "\"")))))




;;
;; Using the Hot Reloadable `defnc` macro
;;

(defnc my-refreshed-component
  [props]
  (let [[count set-count] (react/useState 0)
        [name set-name] (react/useState "React Refresh")]
    (react/useEffect (fn []
                       (let [interval (js/setInterval
                                       #(set-count inc)
                                       1000)]
                         #(js/clearInterval interval)))
                     (array))
    ;; kept it's state!
    ($ :div {:style {:background "pink"}}
       ($ :div "Hello ", name)
       ($ :div ($ :button {:onClick #(set-count inc)} "+ " count))
       ($ :div ($ :input {:type "text"
                          :value name
                          :onChange #(set-name (.. % -target -value))})))))


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
  (fresh/reload))
