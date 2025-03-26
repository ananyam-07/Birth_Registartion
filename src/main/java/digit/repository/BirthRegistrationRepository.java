package digit.repository;

import digit.repository.querybuilder.BirthApplicationQueryBuilder;
import digit.repository.rowmapper.BirthApplicationRowMapper;
import digit.web.models.BirthApplicationSearchCriteria;
import digit.web.models.BirthRegistrationApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository  // Marks this class as a repository component for Spring's component scanning
public class BirthRegistrationRepository {

    @Autowired
    private BirthApplicationQueryBuilder queryBuilder;  // Builds SQL query for birth application search

    @Autowired
    private JdbcTemplate jdbcTemplate;  // Executes SQL queries against the database

    @Autowired
    private BirthApplicationRowMapper rowMapper;  // Maps the SQL result set to BirthRegistrationApplication objects

    /**
     * Retrieves a list of birth registration applications based on search criteria.
     * 
     * @param searchCriteria The criteria used to filter the search
     * @return List of BirthRegistrationApplication matching the search criteria
     */
    public List<BirthRegistrationApplication> getApplications(BirthApplicationSearchCriteria searchCriteria) {
        List<Object> preparedStmtList = new ArrayList<>();  // Holds parameters for the SQL query
        String query = queryBuilder.getBirthApplicationSearchQuery(searchCriteria, preparedStmtList);  // Build the SQL query
        
        log.info("Final query: " + query);  // Log the generated SQL query for debugging or auditing

        // Execute the query and map the result set to a list of BirthRegistrationApplication objects
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }
}
