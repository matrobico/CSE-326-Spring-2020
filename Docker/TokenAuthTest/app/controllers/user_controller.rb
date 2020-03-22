class UserController < ApplicationController
  def new
    @user = User.new
  end

  def create
    @user = User.new(user_params)
    @user.save
  end

  private
      def user_params
        params.require(:user).permit(:name, :password, :password_confirmation)
  end
end
