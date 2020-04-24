Rails.application.routes.draw do
  get 'user/new'
  get 'user/create'
  post 'authenticate', to: 'authentication#authenticate'

  resources :user
  resources :groups do
    resources :messages
  end

end
