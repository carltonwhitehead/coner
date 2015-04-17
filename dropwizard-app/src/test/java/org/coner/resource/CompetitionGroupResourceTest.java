package org.coner.resource;

import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.entity.CompetitionGroup;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.ConerCoreService;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.coner.util.TestConstants;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 *
 */
public class CompetitionGroupResourceTest {

    private final CompetitionGroupBoundary competitionGroupBoundary = mock(CompetitionGroupBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupResource(competitionGroupBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(competitionGroupBoundary, conerCoreService);
    }

    @Test
    public void itShouldGetCompetitionGroup() {
        org.coner.core.domain.CompetitionGroup domainCompetitionGroup = DomainEntityTestUtils.fullCompetitionGroup();
        org.coner.api.entity.CompetitionGroup apiCompetitionGroup = ApiEntityTestUtils.fullCompetitionGroup();

        // sanity check test
        assertThat(domainCompetitionGroup.getId()).isSameAs(TestConstants.COMPETITION_GROUP_ID);
        assertThat(apiCompetitionGroup.getId()).isSameAs(TestConstants.COMPETITION_GROUP_ID);

        when(conerCoreService.getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID))
                .thenReturn(domainCompetitionGroup);
        when(competitionGroupBoundary.toApiEntity(domainCompetitionGroup))
                .thenReturn(apiCompetitionGroup);

        Response competitionGroupResourceContainer = resources.client()
                .target("/competitionGroups/" + TestConstants.COMPETITION_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID);
        verify(competitionGroupBoundary).toApiEntity(domainCompetitionGroup);
        verifyNoMoreInteractions(conerCoreService, competitionGroupBoundary);

        assertThat(competitionGroupResourceContainer).isNotNull();
        assertThat(competitionGroupResourceContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        CompetitionGroup getCompetitionGroupResponse = competitionGroupResourceContainer.readEntity(
                CompetitionGroup.class
        );
        assertThat(getCompetitionGroupResponse)
                .isNotNull()
                .isEqualTo(apiCompetitionGroup);
    }

    @Test
    public void itShouldResponseWithNotFoundWhenCompetitionGroupNotFound() {
        when(conerCoreService.getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID)).thenReturn(null);

        Response response = resources.client()
                .target("/competitionGroups/" + TestConstants.COMPETITION_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(competitionGroupBoundary);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}