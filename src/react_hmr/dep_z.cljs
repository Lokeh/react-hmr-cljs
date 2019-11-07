(ns react-hmr.dep-z
  (:require ["react" :as react]
            [react-hmr.react :refer [$]]
            [react-hmr.refresh :as refresh :refer [defnc defmemo]]
            [react-hmr.dep-b :as b]))

(defnc component [{:keys [name]}]
  (let [[count set-count] (react/useState 0)]
    ($ :div {:style {:background "#efefef"}}
       ($ :div ($ :button {:onClick #(set-count inc)} "+ " count))
       (b/greet name))))

(defmemo memo-component component)
