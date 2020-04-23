class GroupsController < ApplicationController
    before_action :authenticate_request
    def index
        @groups = Group.all

        render json: @groups

      end
    
    def show
        @group = Group.find(params[:id])
    end

    def new
        @group = Group.new
    end

    def create
        @group = Group.new(group_params)
     
        if @group.save
          redirect_to @group
        else
          render 'new'
        end
    end
  

    private
      def group_params
        params.permit(:title, :password)
      end
end
