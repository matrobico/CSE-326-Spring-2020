class UserController < ApplicationController
  before_action :authenticate_request, only: [:groups]

  def new
    @user = User.new
  end

  def create
    @user = User.new(user_params)
    @user.save
  end

  def groups
    render json: @current_user.groups.select(:id, :title)
  end  

  private
  def authenticate_user
    @current_user = AuthorizeApiRequest.call(request.headers).result
    render json: { error: 'Not Authorized (in group)' }, status: 401 unless @group.users.find_by(id: @current_user.id)
  end 

  def user_params
    params.require(:user).permit(:name, :password, :password_confirmation)
  end
end
