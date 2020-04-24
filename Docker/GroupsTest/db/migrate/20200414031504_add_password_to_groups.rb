class AddPasswordToGroups < ActiveRecord::Migration[6.0]
  def change
    add_column :groups, :password, :digest
  end
end
