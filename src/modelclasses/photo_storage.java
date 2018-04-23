package modelclasses;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.CompositePK;
import org.javalite.activejdbc.annotations.Table;

@Table("photo_storage")
@CompositePK("Primary_Key")
public class photo_storage extends Model{
}
